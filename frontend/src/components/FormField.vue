<template>
  <div class="mb-3">
    <label :for="fieldId" class="form-label">
      {{ label }}
      <span v-if="required" class="text-danger">*</span>
    </label>
    <input
        :type="type"
        :id="fieldId"
        class="form-control"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        :required="required"
    />
  </div>
</template>

<script>
export default {
  name: 'FormField',
  props: {
    label: {
      type: String,
      required: true
    },
    type: {
      type: String,
      default: 'text'
    },
    modelValue: {
      type: String,
      required: true
    },
    required: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    fieldId() {
      return `field-${this.label.replace(/\s+/g, '-').toLowerCase()}`;
    }
  }
}
</script>

<style scoped>
.text-danger {
  color: red;
}
</style>
