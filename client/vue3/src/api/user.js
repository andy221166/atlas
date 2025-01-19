import apiClient from "@/api/index";

export const signInApi = (username, password) => {
    return apiClient.post('/api/users/sign-in', { username, password });
};

export const signUpApi = (username, password, firstName, lastName, email, phoneNumber) => {
    return apiClient.post('/api/users/sign-up', { username, password, firstName, lastName, email, phoneNumber });
};

export const signOutApi = async () => apiClient.post('/api/users/sign-out');

export const getProfileApi = async () => apiClient.get('/api/users/profile');
