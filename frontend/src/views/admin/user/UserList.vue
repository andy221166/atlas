<template>
  <div class="container-fluid p-2">
    <h2 class="h2 mb-4">User Management</h2>

    <!-- Search Form -->
    <div class="card mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input v-model="filters.id" type="text" class="form-control" placeholder="User ID" />
          </div>
          <div class="col-md-3">
            <input v-model="filters.username" type="text" class="form-control" placeholder="Username" />
          </div>
          <div class="col-md-3">
            <input v-model="filters.email" type="text" class="form-control" placeholder="Email" />
          </div>
          <div class="col-md-3">
            <input v-model="filters.phoneNumber" type="text" class="form-control" placeholder="Phone Number" />
          </div>
        </div>

        <div class="d-flex justify-content-start mt-4 pt-2 border-top">
          <button @click="applyFilters" class="btn btn-primary me-2">
            <i class="bi bi-search me-1"></i> Search
          </button>
          <button @click="resetFilters" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-counterclockwise me-1"></i> Reset
          </button>
        </div>
      </div>
    </div>

    <!-- Users Table -->
    <div class="card">
      <div v-if="isLoadingUsers" class="text-center py-5">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading users...</span>
        </div>
      </div>
      <div v-else class="card-body">
        <div class="table-responsive">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Username</th>
                <th scope="col">Name</th>
                <th scope="col">Email</th>
                <th scope="col">Phone</th>
                <th scope="col">Role</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id">
                <td>{{ user.id }}</td>
                <td>{{ user.username }}</td>
                <td>{{ user.firstName }} {{ user.lastName }}</td>
                <td>{{ user.email }}</td>
                <td>{{ user.phoneNumber }}</td>
                <td>
                  <span class="badge" :class="user.role === 'ADMIN' ? 'bg-danger' : 'bg-success'">
                    {{ user.role }}
                  </span>
                </td>
              </tr>
              <tr v-if="users.length === 0">
                <td colspan="6" class="text-center text-muted">
                  No users found
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-3">
          <span class="text-muted">
            Showing page {{ metadata.currentPage }} of {{ metadata.totalPages }}
            (Total: {{ metadata.totalRecords }} records)
          </span>
          <div class="btn-group">
            <button @click="changePage(metadata.currentPage - 1)" :disabled="metadata.currentPage <= 1"
              class="btn btn-outline-secondary">
              Previous
            </button>
            <button @click="changePage(metadata.currentPage + 1)"
              :disabled="metadata.currentPage >= metadata.totalPages" class="btn btn-outline-secondary">
              Next
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { listUser, type ListUserFilters, type User } from '@/services/user/user.admin.service';
import { toast } from 'vue3-toastify';

const users = ref<User[]>([]);
const isLoadingUsers = ref(true);

const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0
});
const filters = reactive<ListUserFilters>({
  id: undefined,
  username: undefined,
  email: undefined,
  phoneNumber: undefined,
  page: 1,
  size: 20
});

const applyFilters = async (event?: MouseEvent) => {
  isLoadingUsers.value = true;
  try {
    filters.page = metadata.currentPage;
    const response = await listUser(filters);
    users.value = response.data;
    Object.assign(metadata, response.metadata);
  } catch (error) {
    toast.error('Failed to load users. Please try again.');
  } finally {
    isLoadingUsers.value = false;
  }
};

const resetFilters = () => {
  Object.assign(filters, {
    id: null,
    username: '',
    email: '',
    phoneNumber: '',
    page: 1,
    size: 20
  });
  metadata.currentPage = 1;
  applyFilters();
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= metadata.totalPages) {
    metadata.currentPage = newPage;
    applyFilters();
  }
};

onMounted(() => {
  applyFilters();
});
</script>
