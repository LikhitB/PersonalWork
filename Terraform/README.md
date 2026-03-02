# AWS Security Groups - Quick Notes

## Attaching SG to a resource
- `vpc_security_group_ids` → uses SG **id**, for VPC resources (modern, use this)
- `security_groups` → uses SG **name**, for Classic EC2/ELB (legacy, avoid)
- Mixing both on same resource causes **perpetual diff** in terraform plan

## Defining Rules — 2 ways

**Inline** (inside `aws_security_group` block)
- simpler, but rules are tied to SG lifecycle

**Separate resource** (`aws_vpc_security_group_ingress_rule`)
- verbose, but rules are independent and manageable — AWS recommended

> ⚠️ Never mix both styles on the same SG

## Things to Remember
- protocol = `"tcp"` not `"http"` — AWS only accepts `tcp`, `udp`, `icmp`, `-1`
- `-1` = all traffic (used in egress usually)
- Security groups are **stateful** — inbound allowed = outbound response auto allowed

## Variables and file structure for variables in terraform
- `variables.tf` — defines variables with types and defaults
- `terraform.tfvars` — assigns values to variables (not committed to VCS)
- `outputs.tf` — defines outputs to expose values after apply
-  terraform plan/apply/destroy -var-file="terraform.tfvars"  -> applies the variables from the tfvars file if we dont specify -var-file it will ask us to input the variables values during the plan/apply phase
-  Through system environment variables (e.g., `export TF_VAR_variable_name=value`) — useful for CI/CD pipelines or sensitive data
-  But the env variables should start with `TF_VAR_` in environment variables of the system.

## Difference bw locals and variables
- **Variables**: functions cannot be used here
- **Locals**: can use functions and expressions, but cannot be set from outside (no input) — for internal calculations and DRY code
- locals could contain many values but variables can only contain one value, and variables are used to pass values.
- variables can be overridden by environment variables or tfvars file, but locals cannot be overridden as they are only for internal use within the module.

## Datasource block importance
- Used to fetch existing outside the AWS and put in the resources block of tf.state file.
- Commonly used to get VPC IDs, AMI IDs, or existing SGs to reference in new resources without hardcoding values.

## TRACE LEVELS ARE
- ERROR
- WARN
- INFO 
- DEBUG
- Setting `TF_LOG=DEBUG` gives the most detailed output for troubleshooting.
- Use `TF_LOG_PATH=terraform.log` to save logs to a file for later analysis.
- Be cautious with sensitive information in logs when using DEBUG level, as it may expose secrets or credentials.
- setting the env variable temporary `set TF_LOG=DEBUG` and check `echo ${TF_LOG`
- FOR PERMANANT ADDING OF THE ENV VARIABLES JUST ADD THE VARIABLES IN EDIT ENV VARIABLES  