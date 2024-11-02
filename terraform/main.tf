terraform {
  required_providers {
    docker = {
      source  = "hashicorp/docker"
      version = "~> 2.0"
    }
  }

  required_version = ">= 0.12"
}

provider "docker" {
  host = "unix:///var/run/docker.sock"  # Utilisez le socket local si vous exécutez Docker localement
}

# Définir le volume MySQL
resource "docker_volume" "mysql_volume" {
  name = "mysql1"
}

# Définir le conteneur MySQL
resource "docker_container" "mysql_container" {
  name  = "mysql"
  image = "mysql"
  restart = "unless-stopped"
  env = [
    "MYSQL_ROOT_PASSWORD=root"
  ]
  ports {
    internal = 3306
    external = 3306
  }
  volumes {
    volume_name   = docker_volume.mysql_volume.name
    container_path = "/var/lib/mysql"
  }
}

# Définir l'image pour votre application Java
resource "docker_image" "devops_image" {
  name = "roua863/devops:latest"  # Assurez-vous que cette image est déjà disponible sur Docker Hub
}

# Définir le conteneur pour votre application Java
resource "docker_container" "devops_container" {
  name  = "devops_container"
  image = docker_image.devops_image.image_id  # Changer pour .image_id au lieu de .latest
  ports {
    internal = 8082
    external = 8082
  }
}
