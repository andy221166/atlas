import apiClient from "@/api/index";

export const registerApi = (username, password, firstName, lastName, email, phoneNumber) => {
    return apiClient.post('/api/front/users/register', { username, password, firstName, lastName, email, phoneNumber });
};

export const getProfileApi = async () => apiClient.get('/api/front/users/profile');
