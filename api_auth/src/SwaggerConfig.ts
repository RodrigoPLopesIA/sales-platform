// src/swagger.ts

import swaggerJSDoc from 'swagger-jsdoc';

const options: swaggerJSDoc.Options = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'Sales Plataform - Auth API',
      version: '1.0.0',
      description:
        ' Authentication API for the Sales Platform microservice. Handles user registration, login, credential validation, and JWT generation.',
      contact: {
        name: 'Rodrigo Lopes',
        email: 'rodrigoplopes.dev@gmail.com',
      },
    },
    servers: [
      {
        url: 'http://localhost:8081',
        description: 'Servidor local',
      },
    ],
  },
  apis: ['src/routes/*.ts', 'src/controllers/*.ts'],
};

export const swaggerSpec = swaggerJSDoc(options);
