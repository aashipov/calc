import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    include: ['**/*.test.ts', '**/*e2e-spec.ts'],
    exclude: ['**/node_modules/**', '**/dist/**']
  },
})
