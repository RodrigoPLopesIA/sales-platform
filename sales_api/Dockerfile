# Etapa de build
FROM node:18-alpine AS builder

WORKDIR /sales_app

COPY package.json yarn.lock ./
RUN yarn install

COPY . .
RUN yarn build

# Etapa final
FROM node:18-alpine

WORKDIR /sales_app

COPY package.json yarn.lock ./
RUN yarn install --production

COPY --from=builder /sales_app/dist ./dist

EXPOSE 8082

CMD ["node", "dist/server.js"]
