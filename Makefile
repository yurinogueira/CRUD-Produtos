#!/bin/bash
.PHONY: default
.SILENT:


default:

build-development:
	docker-compose build --force-rm --no-cache --pull

development:
	docker-compose up
