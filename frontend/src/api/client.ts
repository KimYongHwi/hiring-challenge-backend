/**
 * 백엔드는 `/api/v1` 아래에 컨트롤러를 두었습니다.
 * 개발 서버에서는 Vite 프록시가 동일 경로를 Spring Boot로 전달합니다.
 */
const API_BASE = '/api/v1'

/** 백엔드 `{ code, message }` ApiErrorResponse·Spring 기본 JSON 등에서 표시용 문구를 뽑습니다. */
const extractErrorMessage = (status: number, statusText: string, bodyText: string): string => {
  const trimmed = bodyText.trim()
  if (!trimmed) {
    return `${status} ${statusText}`
  }
  try {
    const parsed = JSON.parse(trimmed) as {
      code?: unknown
      message?: unknown
      error?: unknown
      errors?: Array<{ defaultMessage?: unknown }>
    }
    if (typeof parsed.message === 'string' && parsed.message.length > 0) {
      return parsed.message
    }
    if (Array.isArray(parsed.errors) && parsed.errors.length > 0) {
      const parts = parsed.errors
        .map((e) => (typeof e.defaultMessage === 'string' ? e.defaultMessage : ''))
        .filter(Boolean)
      if (parts.length > 0) {
        return parts.join('; ')
      }
    }
    if (typeof parsed.error === 'string' && parsed.error.length > 0) {
      return parsed.error
    }
  } catch {
    return trimmed
  }
  return trimmed
}

export const apiRequest = async <T>(path: string, options?: RequestInit): Promise<T> => {
  const headers = new Headers(options?.headers ?? undefined)
  const hasBody = options?.body !== undefined && options?.body !== null
  if (hasBody && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }

  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers,
  })

  if (!res.ok) {
    const text = await res.text()
    throw new Error(extractErrorMessage(res.status, res.statusText, text))
  }

  if (res.status === 204) {
    return undefined as T
  }

  return res.json() as Promise<T>
}
