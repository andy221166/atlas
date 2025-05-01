import axios from 'axios';
import type { ApiResponseWrapper } from './interfaces/common.interface';
import type { Profile } from './interfaces/user.interface';

export const userService = {
  async fetchProfile(): Promise<ApiResponseWrapper<Profile>> {
    const response = await axios.get<ApiResponseWrapper<Profile>>('/api/common/users/profile');
    return response.data;
  }
}
