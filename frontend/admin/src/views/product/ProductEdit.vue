<template>
  <div class="container-fluid p-2">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="h2">Edit Product</h2>
      <button class="btn btn-outline-secondary" @click="router.back()">
        <i class="bi bi-arrow-left me-1"></i> Back
      </button>
    </div>

    <div v-if="isLoading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else class="card">
      <div class="card-body">
        <form @submit.prevent="handleSubmit">
          <!-- Basic Information -->
          <div class="row g-3">
            <h4 class="h5 mb-3">Product Basic Information</h4>

            <div class="col-md-6">
              <label class="form-label">Product Name <span class="required-field">*</span></label>
              <input v-model="form.name" type="text" class="form-control" required />
            </div>

            <div class="col-md-6">
              <label class="form-label">Price <span class="required-field">*</span></label>
              <div class="input-group">
                <span class="input-group-text">$</span>
                <input v-model.number="form.price" type="number" step="0.01" min="0" class="form-control" required />
              </div>
            </div>

            <div class="col-md-6">
              <label class="form-label">Quantity <span class="required-field">*</span></label>
              <input v-model.number="form.quantity" type="number" min="0" class="form-control" required />
            </div>

            <div class="col-md-6">
              <label class="form-label">Status <span class="required-field">*</span></label>
              <select v-model="form.status" class="form-select" required>
                <option v-for="status in Object.values(ProductStatus)" :key="status" :value="status">
                  {{ formatStatusLabel(status) }}
                </option>
              </select>
            </div>

            <div class="col-md-6">
              <label class="form-label">Available From <span class="required-field">*</span></label>
              <input v-model="form.availableFrom" type="datetime-local" class="form-control" required />
            </div>

            <div class="col-md-6">
              <label class="form-label">Active Status <span class="required-field">*</span></label>
              <div class="form-check form-switch">
                <input v-model="form.isActive" class="form-check-input" type="checkbox" role="switch" id="activeSwitch">
                <label class="form-check-label" for="activeSwitch">Product is active</label>
              </div>
            </div>

            <div class="col-md-6">
              <label class="form-label">Brand <span class="required-field">*</span></label>
              <select v-model.number="form.brandId" class="form-select" required :disabled="!brands.length">
                <option :value="0">Select a brand</option>
                <option v-for="brand in brands" :key="brand.id" :value="brand.id">
                  {{ brand.name }}
                </option>
              </select>
            </div>

            <div class="col-md-6">
              <label class="form-label">Categories <span class="required-field">*</span></label>
              <select v-model="form.categoryIds" multiple class="form-select" required :disabled="!categories.length"
                size="3">
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
              <small class="text-muted">Hold Ctrl/Cmd to select multiple categories</small>
            </div>

            <div class="col-12">
              <label class="form-label">Product Image</label>
              <input type="file" class="form-control" @change="handleImageUpload" accept="image/*" />
              <div v-if="imagePreview || form.image" class="mt-2">
                <img :src="imagePreview || form.image" alt="Preview" class="img-thumbnail" style="max-height: 200px" />
              </div>
            </div>
          </div>

          <!-- Product Details -->
          <div class="mt-5">
            <h4 class="h5 mb-3">Product Details</h4>
            <div class="row">
              <div class="col-12">
                <label class="form-label">Description <span class="required-field">*</span></label>
                <textarea v-model="form.details.description" class="form-control" rows="5" required></textarea>
              </div>
            </div>
          </div>

          <!-- Product Attributes -->
          <div class="mt-5">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <h4 class="h5 mb-0">Product Attributes</h4>
              <button type="button" class="btn btn-sm btn-outline-primary" @click="addAttribute">
                <i class="bi bi-plus"></i> Add Attribute
              </button>
            </div>
            <div v-for="(attr, index) in form.attributes" :key="index" class="row g-2 mb-2">
              <div class="col-md-5">
                <input v-model="attr.name" type="text" class="form-control" placeholder="Attribute name" />
              </div>
              <div class="col-md-5">
                <input v-model="attr.value" type="text" class="form-control" placeholder="Attribute value" />
              </div>
              <div class="col-md-2">
                <button type="button" class="btn btn-outline-danger" @click="removeAttribute(index)">
                  <i class="bi bi-trash"></i>
                </button>
              </div>
            </div>
          </div>

          <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
            <button type="button" class="btn btn-outline-secondary" @click="router.back()">Cancel</button>
            <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
              <span v-if="isSubmitting" class="spinner-border spinner-border-sm me-1"></span>
              Update Product
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { listBrand, type Brand } from '@/services/product/brand.service';
import { listCategory, type Category } from '@/services/product/category.service';
import {
  ProductStatus,
  getProduct,
  updateProduct,
  type UpdateProductRequest
} from '@/services/product/product.service';
import { toast } from 'vue3-toastify';

const router = useRouter();
const route = useRoute();
const brands = ref<Brand[]>([]);
const categories = ref<Category[]>([]);
const isSubmitting = ref(false);
const isLoading = ref(true);
const imagePreview = ref<string | null>(null);

const form = reactive<UpdateProductRequest>({
  id: Number(route.params.id),
  name: '',
  price: 0,
  image: '',
  quantity: 0,
  status: ProductStatus.IN_STOCK,
  availableFrom: '',
  isActive: true,
  brandId: 0,
  categoryIds: [],
  details: {
    description: ''
  },
  attributes: []
});

const formatStatusLabel = (status: ProductStatus): string => {
  return status.split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

const handleImageUpload = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = (e) => {
      const result = e.target?.result as string;
      imagePreview.value = result;
      form.image = result;
    };

    reader.readAsDataURL(file);
  }
};

const addAttribute = () => {
  form.attributes.push({ id: 0, name: '', value: '' });
};

const removeAttribute = (index: number) => {
  form.attributes.splice(index, 1);
};

const loadProduct = async () => {
  try {
    const response = await getProduct(form.id);
    if (response.success) {
      const product = response.data;

      // Update form with existing product data
      Object.assign(form, {
        name: product.name,
        price: product.price,
        image: product.image,
        quantity: product.quantity,
        status: product.status,
        availableFrom: new Date(product.availableFrom).toISOString().slice(0, 16),
        isActive: product.isActive,
        brandId: product.brand.id,
        details: product.details,
        attributes: product.attributes,
        categoryIds: product.categories.map(c => c.id)
      });
    }
  } catch (error) {
    toast.error('Failed to load product');
    router.back();
  } finally {
    isLoading.value = false;
  }
};

const handleSubmit = async () => {
  try {
    if (form.brandId === 0) {
      toast.error('Please select a brand');
      return;
    }

    if (form.categoryIds.length === 0) {
      toast.error('Please select at least one category');
      return;
    }

    isSubmitting.value = true;

    // Remove empty attributes
    form.attributes = form.attributes.filter(attr => attr.name.trim() && attr.value.trim());

    // Format date to ISO string
    const formData: UpdateProductRequest = {
      ...form,
      availableFrom: new Date(form.availableFrom).toISOString()
    };

    const response = await updateProduct(formData);

    if (response.success) {
      toast.success('Product updated successfully');
      router.push({ name: 'productList' });
    }
  } finally {
    isSubmitting.value = false;
  }
};

const loadBrandsAndCategories = async () => {
  const [brandsResponse, categoriesResponse] = await Promise.all([
    listBrand(),
    listCategory()
  ]);

  if (brandsResponse.success) {
    brands.value = brandsResponse.data;
  }
  if (categoriesResponse.success) {
    categories.value = categoriesResponse.data;
  }
};

onMounted(async () => {
  await Promise.all([
    loadBrandsAndCategories(),
    loadProduct()
  ]);
});
</script>
