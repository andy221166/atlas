import type { OrderStatus } from "../../types/order.interface";

export const formatOrderStatusLabel = (status: OrderStatus): string => {
    return status
        .split('_')
        .map(word => word.charAt(0) + word.slice(1).toLowerCase())
        .join(' ');
};
