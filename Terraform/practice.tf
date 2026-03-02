provider "aws" {
    region = "ap-south-1"
}

# # count,variables,tags
# resource "aws_instance" "project" {
#     ami="ami-051a31ab2f4d498f5"
#     instance_type = var.instance_type
#     tags={
#       Name=var.names[count.index]
#     }
#     count=3
# }
#
# # functions
# resource "aws_iam_user" "FunctionUser" {
#     name="FunctionUser"
# }
# resource "aws_iam_user_policy" "Ec2RdsPolicy" {
#     name="Ec2RdsPolicy"
#     user=aws_iam_user.FunctionUser.name
#     policy = file("C:/Users/VLIKHBA/IdeaProjects/PersonalWork/Terraform/policy.json")
# }
#
# # dataSource
# data "aws_account_regions" "example" {}
# data "aws_instance" "instanceData" {
#     filter {
#         name="tag:developer"
#         values=["dev"]
#     }
# }
#datasource for creating the dynamic latest AMI
data "aws_ami" "latest_ami"{
    most_recent = true
    owners=["amazon"]
    filter {
        name="name"
        values=["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
    }
}
resource "aws_instance" "new_instance"{
    ami=data.aws_ami.latest_ami.id
    tags={
        Name="LatesAMIinstance"
    }
    instance_type = var.instance_type
}
# }
# output "lookup" {
#     value= lookup(var.mymap,"name","No name present")
# }
# output "length" {
#     value= length(var.mymap)
# }
# output "element"{
#     value= element(var.names,3)
# }
#
# output "local_variable" {
#     value= local.lenOfDefault
# }
#
# output "data_source_trail"{
#     value=data.aws_account_regions.example.regions.*
# }

