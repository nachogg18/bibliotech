# bibliotech

bibliotech project

Java 17

## **Kubernetes**


### instalar minikube
`kubectl create configmap app-configs --from-env-file=./.env`

### iniciar deployment

`kubectl apply -f k8s-deployment.yaml`

### iniciar service

`kubectl apply -f k8s-service.yaml `

### iniciar ingress

`kubectl apply -f k8s-ingress.yaml `

### para exponer port forward y probar

`kubectl port-forward deployment/spring-boot-k8s 8080:8080`