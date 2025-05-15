import type { ProductStatus } from "../types/product.enum";

export const getProductImageUrl = (image: string | null): string => {
    if (!image) {
        // Handle null/empty case
        return new URL('@/assets/images/product-placeholder.jpg', import.meta.url).href;
    }

    // Handle both URL and base64 cases - they can be used directly in src
    return image;
};

export const formatProductStatusLabel = (status: ProductStatus): string => {
    return status.split('_')
        .map(word => word.charAt(0) + word.slice(1).toLowerCase())
        .join(' ');
};
