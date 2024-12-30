#!/bin/bash

aws ecr create-repository --repository-name nv-home-assignment --region us-west-1
aws ecr get-login-password --region us-west-1 | docker login --username AWS --password-stdin 340752837068.dkr.ecr.us-west-1.amazonaws.com

