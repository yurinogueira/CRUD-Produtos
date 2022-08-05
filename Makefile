#!/bin/bash
.PHONY: default
.SILENT:


default:

build-development:
	rm -rf spring/target/
	rm -rf vue/node_modules/
	docker-compose build --force-rm --no-cache --pull

development:
	docker-compose up
