export const validators = {
  email: (value: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return emailRegex.test(value)
  },

  required: (value: any): boolean => {
    return value !== null && value !== undefined && value !== ''
  },

  minLength: (min: number) => (value: string): boolean => {
    return value && value.length >= min
  },

  maxLength: (max: number) => (value: string): boolean => {
    return !value || value.length <= max
  }
}

export const validateForm = (data: Record<string, any>, rules: Record<string, Function[]>) => {
  const errors: Record<string, string[]> = {}
  
  for (const [field, fieldRules] of Object.entries(rules)) {
    const fieldErrors: string[] = []
    const value = data[field]
    
    for (const rule of fieldRules) {
      if (!rule(value)) {
        fieldErrors.push(`${field} validation failed`)
      }
    }
    
    if (fieldErrors.length > 0) {
      errors[field] = fieldErrors
    }
  }
  
  return {
    isValid: Object.keys(errors).length === 0,
    errors
  }
} 