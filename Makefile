# Variables for paths (adjust if your folders have different names)
FRONTEND_DIR = frontend
BACKEND_DIR = backend

.PHONY: help install run-front run-back run-all

help:
	@echo "Available commands:"
	@echo "  make install    - Clean and compile backend"
	@echo "  make run-front  - Start Angular frontend"
	@echo "  make run-back   - Start Maven backend"
	@echo "  make run-all    - Start both (requires manual stop)"

# Dependency: mvn clean compile
install:
	cd $(BACKEND_DIR) && mvn -e clean compile

# Frontend: ng serve
run-front:
	cd $(FRONTEND_DIR) && ng serve

# Backend: mvn exec:java...
run-back:
	cd $(BACKEND_DIR) && mvn exec:java -Dexec.mainClass="com.myapp.App"

# Run both (Parallel)
run-all:
	make -j 2 run-front run-back