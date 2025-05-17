export const getRoleBadgeClasses = (role: string | null): string => {
  if (!role) {
    return '';
  }
  switch (role.toUpperCase()) {
    case 'ADMIN':
      return 'badge bg-danger';
    default:
      return '';
  }
}
