resource "aws_elb" "threeTierELB" {
  name = "MyELB"
  listener {
    instance_port     = 8080
    instance_protocol = ""
    lb_port           = 80
    lb_protocol       = "http"
  } subnets = module.VPC.public_subnet_ids
