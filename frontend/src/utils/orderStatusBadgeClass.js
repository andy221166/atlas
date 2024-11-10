export const applyBadgeClass = (orderStatus) => {
  switch (orderStatus.toUpperCase()) {
    case "PROCESSING":
      return "badge bg-warning text-dark";
    case "CONFIRMED":
      return "badge bg-success";
    case "CANCELED":
      return "badge bg-danger";
    default:
      return "badge bg-secondary"; // Fallback for unknown statuses
  }
};
