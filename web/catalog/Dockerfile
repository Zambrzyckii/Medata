# Build stage: compile the Angular production bundle
FROM node:22-alpine AS build
WORKDIR /workspace
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# Runtime stage: NGINX serves the bundle and proxies /api to the gateway.
# The official image runs envsubst on /etc/nginx/templates/*.template at startup,
# which is how GATEWAY_URL and NGINX_PORT below end up in the real config.
FROM nginx:1.29-alpine
COPY nginx/default.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /workspace/dist/catalog/browser /usr/share/nginx/html
ENV GATEWAY_URL=http://localhost:8080 \
    NGINX_PORT=80
EXPOSE 80
