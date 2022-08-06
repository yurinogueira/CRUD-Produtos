# Build the web
FROM node:lts-alpine as crud-vue-builder

WORKDIR /app

COPY ./vue/package*.json ./

RUN npm install

COPY ./vue/ .

RUN npm run build

# Run the web
FROM nginx:stable-alpine as crud-vue

LABEL maintainer="yurinogueira@id.uff.br"
LABEL vendor="Nogueira Tecnologia"

COPY --from=crud-vue-builder /app/dist /usr/share/nginx/html
COPY ./docker/nginx/prod.conf /temp/prod.conf

RUN envsubst /app < /temp/prod.conf > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
