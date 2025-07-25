FROM node:18-alpine AS builder

WORKDIR /app_auth

COPY package.json yarn.lock ./
RUN yarn install

COPY . .
RUN yarn build


FROM node:18-alpine

WORKDIR /app_auth

COPY package.json yarn.lock ./
RUN yarn install --production

COPY --from=builder /app_auth/dist ./dist
EXPOSE 8080
CMD ["node", "dist/server.js"]
