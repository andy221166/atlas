import type { Product } from "@/features/product/types/product.interface";

export interface CartItem {
  product: Product;
  quantity: number;
}
