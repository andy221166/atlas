export function applyBadgeClass(status) {
  if (!status) {
    return 'badge bg-primary';
  }
  switch (status.toUpperCase()) {
    case 'PROCESSING':
      return 'badge bg-warning text-dark';
    case 'CONFIRMED':
      return 'badge bg-success';
    case 'CANCELED':
      return 'badge bg-danger';
    default:
      return 'badge bg-primary';
  }
}
