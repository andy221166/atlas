// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: product.proto

// Protobuf Java Version: 3.25.1
package org.atlas.platform.grpc.protobuf.product;

public interface ProductListProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:product.ProductListProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .product.ProductProto product = 1;</code>
   */
  java.util.List<org.atlas.platform.grpc.protobuf.product.ProductProto>
  getProductList();

  /**
   * <code>repeated .product.ProductProto product = 1;</code>
   */
  org.atlas.platform.grpc.protobuf.product.ProductProto getProduct(int index);

  /**
   * <code>repeated .product.ProductProto product = 1;</code>
   */
  int getProductCount();

  /**
   * <code>repeated .product.ProductProto product = 1;</code>
   */
  java.util.List<? extends org.atlas.platform.grpc.protobuf.product.ProductProtoOrBuilder>
  getProductOrBuilderList();

  /**
   * <code>repeated .product.ProductProto product = 1;</code>
   */
  org.atlas.platform.grpc.protobuf.product.ProductProtoOrBuilder getProductOrBuilder(
      int index);
}
