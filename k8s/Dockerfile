# Utilise une image Node.js pour construire l'application
FROM node:14 AS build

# Définit le répertoire de travail
WORKDIR /app

# Copie les fichiers package.json et package-lock.json
COPY package*.json ./

# Installe les dépendances
RUN npm install

# Copie le reste des fichiers de l'application
COPY . .

# Construit l'application Angular
RUN npm run build --prod

# Étape pour servir l'application
FROM nginx:alpine

# Copie les fichiers construits dans le répertoire de Nginx
COPY --from=build /app/dist/mon-application-angular /usr/share/nginx/html

# Expose le port 80
EXPOSE 80

# Commande par défaut pour Nginx
CMD ["nginx", "-g", "daemon off;"]
