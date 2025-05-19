<template>
  <div class="container-fluid p-2">
    <h2 class="h2 mb-4">User Management</h2>

    <!-- Search Form -->
    <div class="card mb-4">
      <div class="card-body">
        <form @submit.prevent="applyFilters(1)">
          <div class="row g-3">
            <div class="col-md-3">
              <label for="userId" class="form-label">User ID</label>
              <input
                id="userId"
                v-model.trim="filters.id"
                type="text"
                class="form-control"
                placeholder="Enter user ID"
              />
            </div>
            <div class="col-md-3">
              <label for="keyword" class="form-label">Keyword</label>
              <input
                id="keyword"
                v-model.trim="filters.keyword"
                type="text"
                class="form-control"
                placeholder="Search by username, name, or email"
              />
            </div>
          </div>
          <div class="d-flex gap-2 mt-4 pt-2 border-top">
            <button type="submit" class="btn btn-primary">
              <i class="bi bi-search me-1"></i> Search
            </button>
            <button type="button" @click="resetFilters" class="btn btn-outline-secondary">
              <i class="bi bi-arrow-counterclockwise me-1"></i> Reset
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Users Table -->
    <div class="card">
      <div v-if="isLoadingUsers" class="text-center py-5" aria-live="polite">
        <div class="spinner-border" role="status" aria-label="Loading users">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
      <div v-else class="card-body">
        <div class="table-responsive">
          <table class="table table-striped table-hover">
            <thead class="table-light">
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
                <td>{{ user.phoneNumber || 'N/A' }}</td>
                <td>
                  <span :class="getRoleBadgeClasses(user.role)">
                    {{ user.role }}
                  </span>
                </td>
              </tr>
              <tr v-if="!users.length">
                <td colspan="6" class="text-center text-muted">
                  No users found
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="card-footer bg-light">
          <div class="d-flex justify-content-between align-items-center">
            <span class="text-muted">
              Page {{ metadata.currentPage }} of {{ metadata.totalPages }}
              ({{ metadata.totalRecords }} records)
            </span>
            <div class="btn-group">
              <button
                @click="changePage(metadata.currentPage - 1)"
                :disabled="metadata.currentPage <= 1"
                class="btn btn-outline-secondary"
              >
                Previous
              </button>
              <button
                @click="changePage(metadata.currentPage + 1)"
                :disabled="metadata.currentPage >= metadata.totalPages"
                class="btn btn-outline-secondary"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ListUserFilters, User } from '@/types/user.interface';
import { onMounted, reactive, ref } from 'vue';
import { toast } from 'vue3-toastify';
import { listUser } from '../../services/user.admin.service';
import { getRoleBadgeClasses } from '../../utils/user.util';

const users = ref<User[]>([]);
const isLoadingUsers = ref(true);
const metadata = reactive({
  currentPage: 1,
  pageSize: 20,
  totalPages: 1,
  totalRecords: 0,
});
const filters = reactive<ListUserFilters>({
  id: undefined,
  keyword: undefined,
  page: 1,
  size: 20,
});

const applyFilters = async (page: number) => {
  if (isLoadingUsers.value) return;

  isLoadingUsers.value = true;
  try {
    filters.page = page;
    metadata.currentPage = page;

    // Clean up empty or undefined filters
    const apiFilters: ListUserFilters = { ...filters };
    Object.keys(apiFilters).forEach((key) => {
      const typedKey = key as keyof ListUserFilters;
      if (apiFilters[typedKey] === '' || apiFilters[typedKey] === undefined) {
        delete apiFilters[typedKey];
      }
    });

    const response = await listUser(apiFilters);
    users.value = response.data;
    Object.assign(metadata, response.metadata);
  } catch (error) {
    toast.error('Failed to load users');
  } finally {
    isLoadingUsers.value = false;
  }
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= metadata.totalPages) {
    applyFilters(newPage);
  }
};

const resetFilters = () => {
  Object.assign(filters, {
    id: undefined,
    keyword: undefined,
    page: 1,
    size: 20,
  });
  applyFilters(1);
};

onMounted(() => applyFilters(1));
</script>

<style scoped>
.table-responsive {
  min-height: 200px;
}
</style>
