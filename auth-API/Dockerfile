FROM node:14
WORKDIR .
COPY packge*.json ./
RUN yarn
COPY . .
EXPOSE 8080
CMD [ "node", "app.js" ]
