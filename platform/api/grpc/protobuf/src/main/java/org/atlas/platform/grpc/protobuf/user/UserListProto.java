// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user.proto

// Protobuf Java Version: 3.25.1
package org.atlas.platform.grpc.protobuf.user;

/**
 * Protobuf type {@code user.UserListProto}
 */
public final class UserListProto extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:user.UserListProto)
    UserListProtoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use UserListProto.newBuilder() to construct.
  private UserListProto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private UserListProto() {
    user_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new UserListProto();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.atlas.platform.grpc.protobuf.user.User.internal_static_user_UserListProto_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.atlas.platform.grpc.protobuf.user.User.internal_static_user_UserListProto_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.atlas.platform.grpc.protobuf.user.UserListProto.class, org.atlas.platform.grpc.protobuf.user.UserListProto.Builder.class);
  }

  public static final int USER_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private java.util.List<org.atlas.platform.grpc.protobuf.user.UserProto> user_;
  /**
   * <code>repeated .user.UserProto user = 1;</code>
   */
  @java.lang.Override
  public java.util.List<org.atlas.platform.grpc.protobuf.user.UserProto> getUserList() {
    return user_;
  }
  /**
   * <code>repeated .user.UserProto user = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder> 
      getUserOrBuilderList() {
    return user_;
  }
  /**
   * <code>repeated .user.UserProto user = 1;</code>
   */
  @java.lang.Override
  public int getUserCount() {
    return user_.size();
  }
  /**
   * <code>repeated .user.UserProto user = 1;</code>
   */
  @java.lang.Override
  public org.atlas.platform.grpc.protobuf.user.UserProto getUser(int index) {
    return user_.get(index);
  }
  /**
   * <code>repeated .user.UserProto user = 1;</code>
   */
  @java.lang.Override
  public org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder getUserOrBuilder(
      int index) {
    return user_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < user_.size(); i++) {
      output.writeMessage(1, user_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < user_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, user_.get(i));
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.atlas.platform.grpc.protobuf.user.UserListProto)) {
      return super.equals(obj);
    }
    org.atlas.platform.grpc.protobuf.user.UserListProto other = (org.atlas.platform.grpc.protobuf.user.UserListProto) obj;

    if (!getUserList()
        .equals(other.getUserList())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getUserCount() > 0) {
      hash = (37 * hash) + USER_FIELD_NUMBER;
      hash = (53 * hash) + getUserList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.atlas.platform.grpc.protobuf.user.UserListProto parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.atlas.platform.grpc.protobuf.user.UserListProto prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code user.UserListProto}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:user.UserListProto)
      org.atlas.platform.grpc.protobuf.user.UserListProtoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.atlas.platform.grpc.protobuf.user.User.internal_static_user_UserListProto_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.atlas.platform.grpc.protobuf.user.User.internal_static_user_UserListProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.atlas.platform.grpc.protobuf.user.UserListProto.class, org.atlas.platform.grpc.protobuf.user.UserListProto.Builder.class);
    }

    // Construct using org.atlas.platform.grpc.protobuf.user.UserListProto.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (userBuilder_ == null) {
        user_ = java.util.Collections.emptyList();
      } else {
        user_ = null;
        userBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.atlas.platform.grpc.protobuf.user.User.internal_static_user_UserListProto_descriptor;
    }

    @java.lang.Override
    public org.atlas.platform.grpc.protobuf.user.UserListProto getDefaultInstanceForType() {
      return org.atlas.platform.grpc.protobuf.user.UserListProto.getDefaultInstance();
    }

    @java.lang.Override
    public org.atlas.platform.grpc.protobuf.user.UserListProto build() {
      org.atlas.platform.grpc.protobuf.user.UserListProto result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.atlas.platform.grpc.protobuf.user.UserListProto buildPartial() {
      org.atlas.platform.grpc.protobuf.user.UserListProto result = new org.atlas.platform.grpc.protobuf.user.UserListProto(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(org.atlas.platform.grpc.protobuf.user.UserListProto result) {
      if (userBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          user_ = java.util.Collections.unmodifiableList(user_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.user_ = user_;
      } else {
        result.user_ = userBuilder_.build();
      }
    }

    private void buildPartial0(org.atlas.platform.grpc.protobuf.user.UserListProto result) {
      int from_bitField0_ = bitField0_;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.atlas.platform.grpc.protobuf.user.UserListProto) {
        return mergeFrom((org.atlas.platform.grpc.protobuf.user.UserListProto)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.atlas.platform.grpc.protobuf.user.UserListProto other) {
      if (other == org.atlas.platform.grpc.protobuf.user.UserListProto.getDefaultInstance()) return this;
      if (userBuilder_ == null) {
        if (!other.user_.isEmpty()) {
          if (user_.isEmpty()) {
            user_ = other.user_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureUserIsMutable();
            user_.addAll(other.user_);
          }
          onChanged();
        }
      } else {
        if (!other.user_.isEmpty()) {
          if (userBuilder_.isEmpty()) {
            userBuilder_.dispose();
            userBuilder_ = null;
            user_ = other.user_;
            bitField0_ = (bitField0_ & ~0x00000001);
            userBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getUserFieldBuilder() : null;
          } else {
            userBuilder_.addAllMessages(other.user_);
          }
        }
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              org.atlas.platform.grpc.protobuf.user.UserProto m =
                  input.readMessage(
                      org.atlas.platform.grpc.protobuf.user.UserProto.parser(),
                      extensionRegistry);
              if (userBuilder_ == null) {
                ensureUserIsMutable();
                user_.add(m);
              } else {
                userBuilder_.addMessage(m);
              }
              break;
            } // case 10
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.util.List<org.atlas.platform.grpc.protobuf.user.UserProto> user_ =
      java.util.Collections.emptyList();
    private void ensureUserIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        user_ = new java.util.ArrayList<org.atlas.platform.grpc.protobuf.user.UserProto>(user_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.atlas.platform.grpc.protobuf.user.UserProto, org.atlas.platform.grpc.protobuf.user.UserProto.Builder, org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder> userBuilder_;

    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public java.util.List<org.atlas.platform.grpc.protobuf.user.UserProto> getUserList() {
      if (userBuilder_ == null) {
        return java.util.Collections.unmodifiableList(user_);
      } else {
        return userBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public int getUserCount() {
      if (userBuilder_ == null) {
        return user_.size();
      } else {
        return userBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public org.atlas.platform.grpc.protobuf.user.UserProto getUser(int index) {
      if (userBuilder_ == null) {
        return user_.get(index);
      } else {
        return userBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder setUser(
        int index, org.atlas.platform.grpc.protobuf.user.UserProto value) {
      if (userBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureUserIsMutable();
        user_.set(index, value);
        onChanged();
      } else {
        userBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder setUser(
        int index, org.atlas.platform.grpc.protobuf.user.UserProto.Builder builderForValue) {
      if (userBuilder_ == null) {
        ensureUserIsMutable();
        user_.set(index, builderForValue.build());
        onChanged();
      } else {
        userBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder addUser(org.atlas.platform.grpc.protobuf.user.UserProto value) {
      if (userBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureUserIsMutable();
        user_.add(value);
        onChanged();
      } else {
        userBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder addUser(
        int index, org.atlas.platform.grpc.protobuf.user.UserProto value) {
      if (userBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureUserIsMutable();
        user_.add(index, value);
        onChanged();
      } else {
        userBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder addUser(
        org.atlas.platform.grpc.protobuf.user.UserProto.Builder builderForValue) {
      if (userBuilder_ == null) {
        ensureUserIsMutable();
        user_.add(builderForValue.build());
        onChanged();
      } else {
        userBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder addUser(
        int index, org.atlas.platform.grpc.protobuf.user.UserProto.Builder builderForValue) {
      if (userBuilder_ == null) {
        ensureUserIsMutable();
        user_.add(index, builderForValue.build());
        onChanged();
      } else {
        userBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder addAllUser(
        java.lang.Iterable<? extends org.atlas.platform.grpc.protobuf.user.UserProto> values) {
      if (userBuilder_ == null) {
        ensureUserIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, user_);
        onChanged();
      } else {
        userBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder clearUser() {
      if (userBuilder_ == null) {
        user_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        userBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public Builder removeUser(int index) {
      if (userBuilder_ == null) {
        ensureUserIsMutable();
        user_.remove(index);
        onChanged();
      } else {
        userBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public org.atlas.platform.grpc.protobuf.user.UserProto.Builder getUserBuilder(
        int index) {
      return getUserFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder getUserOrBuilder(
        int index) {
      if (userBuilder_ == null) {
        return user_.get(index);  } else {
        return userBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public java.util.List<? extends org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder> 
         getUserOrBuilderList() {
      if (userBuilder_ != null) {
        return userBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(user_);
      }
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public org.atlas.platform.grpc.protobuf.user.UserProto.Builder addUserBuilder() {
      return getUserFieldBuilder().addBuilder(
          org.atlas.platform.grpc.protobuf.user.UserProto.getDefaultInstance());
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public org.atlas.platform.grpc.protobuf.user.UserProto.Builder addUserBuilder(
        int index) {
      return getUserFieldBuilder().addBuilder(
          index, org.atlas.platform.grpc.protobuf.user.UserProto.getDefaultInstance());
    }
    /**
     * <code>repeated .user.UserProto user = 1;</code>
     */
    public java.util.List<org.atlas.platform.grpc.protobuf.user.UserProto.Builder> 
         getUserBuilderList() {
      return getUserFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.atlas.platform.grpc.protobuf.user.UserProto, org.atlas.platform.grpc.protobuf.user.UserProto.Builder, org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder> 
        getUserFieldBuilder() {
      if (userBuilder_ == null) {
        userBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.atlas.platform.grpc.protobuf.user.UserProto, org.atlas.platform.grpc.protobuf.user.UserProto.Builder, org.atlas.platform.grpc.protobuf.user.UserProtoOrBuilder>(
                user_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        user_ = null;
      }
      return userBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:user.UserListProto)
  }

  // @@protoc_insertion_point(class_scope:user.UserListProto)
  private static final org.atlas.platform.grpc.protobuf.user.UserListProto DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.atlas.platform.grpc.protobuf.user.UserListProto();
  }

  public static org.atlas.platform.grpc.protobuf.user.UserListProto getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<UserListProto>
      PARSER = new com.google.protobuf.AbstractParser<UserListProto>() {
    @java.lang.Override
    public UserListProto parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<UserListProto> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<UserListProto> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.atlas.platform.grpc.protobuf.user.UserListProto getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

