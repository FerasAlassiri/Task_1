import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  // Only touch calls to our API
  if (!req.url.startsWith(environment.apiBase)) {
    return next(req);
  }

  // If caller already set Authorization, DON'T override it
  if (req.headers.has('Authorization')) {
    return next(req);
  }

  const token = localStorage.getItem('basicCreds');
  if (token) {
    const authReq = req.clone({
      setHeaders: { Authorization: `Basic ${token}` }
    });
    return next(authReq);
  }

  return next(req);
};