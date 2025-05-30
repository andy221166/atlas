import { ref, type Ref } from 'vue'

export interface UseAsyncReturn<T> {
  data: Ref<T | null>
  loading: Ref<boolean>
  error: Ref<string | null>
  execute: (...args: any[]) => Promise<T | null>
  reset: () => void
}

export function useAsync<T>(
  asyncFunction: (...args: any[]) => Promise<T>
): UseAsyncReturn<T> {
  const data = ref<T | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const execute = async (...args: any[]): Promise<T | null> => {
    try {
      loading.value = true
      error.value = null
      const result = await asyncFunction(...args)
      data.value = result
      return result
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'An error occurred'
      return null
    } finally {
      loading.value = false
    }
  }

  const reset = () => {
    data.value = null
    loading.value = false
    error.value = null
  }

  return {
    data,
    loading,
    error,
    execute,
    reset
  }
} 