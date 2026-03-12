
variable "public_cidr" {
  type=list(string)
  default = ["10.0.6.0/24","10.0.7.0/24"]
}
variable "public_cidr_names" {
  type=list(string)
  default = ["public_subnet1","public_subnet2"]
}
variable "private_cidr" {
  type = map(string)
  default = {
    "webTeirSb1":"10.0.4.0/24",
    "webTeirsb2":"10.0.5.0/24",
    "AppTeirSb1":"10.0.0.0/24",
    "AppTeirSb2":"10.0.1.0/24",
    "DBTeirSb1":"10.0.2.0/24",
    "DBTeirSb2":"10.0.3.0/24"

  }
}