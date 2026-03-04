# variable "ports" {}
# instance_type comes from the user variables i.e. environment variables in the system.
variable "instance_type" {} #
variable "names" {
  type    = list(string)
  default = ["WebServer", "AppServer", "DBServer"]
}
variable "mymap" {
  type = map(string)
  default = {
    "name" = "Likhit"
    "age"  = "24"
    "city" = "Visakhapatnam"
  }
}

locals {
  default       = "This is a local variable"
  lenOfDefault  = length(local.default)
  ingress_ports = [80, 443, 8080, 3306, 6379]
}

