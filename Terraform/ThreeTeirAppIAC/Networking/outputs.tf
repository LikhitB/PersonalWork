output "public_subnet_ids" {
  value = aws_subnet.publicSubnets[*].id
}
