// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user.proto

// Protobuf Java Version: 3.25.1
package org.atlas.platform.grpc.protobuf.user;

public interface UserProtoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:user.UserProto)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 id = 1;</code>
   *
   * @return The id.
   */
  int getId();

  /**
   * <code>string email = 2;</code>
   *
   * @return The email.
   */
  java.lang.String getEmail();

  /**
   * <code>string email = 2;</code>
   *
   * @return The bytes for email.
   */
  com.google.protobuf.ByteString
  getEmailBytes();

  /**
   * <code>string first_name = 3;</code>
   *
   * @return The firstName.
   */
  java.lang.String getFirstName();

  /**
   * <code>string first_name = 3;</code>
   *
   * @return The bytes for firstName.
   */
  com.google.protobuf.ByteString
  getFirstNameBytes();

  /**
   * <code>string last_name = 4;</code>
   *
   * @return The lastName.
   */
  java.lang.String getLastName();

  /**
   * <code>string last_name = 4;</code>
   *
   * @return The bytes for lastName.
   */
  com.google.protobuf.ByteString
  getLastNameBytes();

  /**
   * <code>string phone_number = 5;</code>
   *
   * @return The phoneNumber.
   */
  java.lang.String getPhoneNumber();

  /**
   * <code>string phone_number = 5;</code>
   *
   * @return The bytes for phoneNumber.
   */
  com.google.protobuf.ByteString
  getPhoneNumberBytes();
}