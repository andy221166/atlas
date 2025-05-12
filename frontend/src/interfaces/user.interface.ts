export interface User {
  id: number
  username: string
  firstName: string
  lastName: string
  email: string
  phoneNumber: string
  role: string
}

export interface Profile {
  firstName: string
  lastName: string
  role: string
}

export interface ListUserFilters {
  id?: string
  username?: string
  email?: string
  phoneNumber?: string
  page: number
  size: number
}

export interface RegisterRequest {
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
}
