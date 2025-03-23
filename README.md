<h1 align="center">
DefineX Final Project</h1>

<p align="center">
  <img src="https://img.shields.io/badge/-Java-DD052B?style=flat&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white">
  
</p>

Subject: Advanced Task Management  

Story: The current application has become outdated, slow, and no longer meets the evolving needs of the corporation. After conducting extensive research into alternative tools, the company board has decided to develop an Advanced Task Management application in-house. This repository contains the implementation of the new, modernized solution, designed to streamline task management, enhance performance, and meet the current and future requirements of the organization.

<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#key-features">Key Features</a>
    </li>
    <li><a href="#project-structure">Project Structure</a></li>
    <li><a href="#used-technologies">Used Technologies</a></li>
    <li><a href="architecture">Architecture</a></li>
    <li><a href="#api-documentation">API Documentation</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#licence">Licence</a></li>
  </ol>
</details>

## Key Features
* Project and Task Management: The application allows project group managers to manage projects related to a department and also manage tasks within a project.
* Team Member Assignment: Team leaders or project managers (in a department) can assign team members to tasks.
* Progress Tracking: Team leaders or project managers can track the progress of a task, with states such as Backlog, In Analysis, In Development/Progress, Cancelled, Blocked, or Completed.
* Priority Management: Team leaders or project managers can assign priorities to tasks, including Critical, High, Medium, or Low.
* File Attachment Support: Team members (Project Managers, Team Leaders, or other Team Members) can attach one or more files to a task, with the files being stored on the disk.

## Project Structure
![Multı-Module](https://github.com/user-attachments/assets/7424a07a-ca59-450b-aeb7-ac5e0cce9b2c)

## Used Technologies
* Java 21
* Spring Boot
* Microservice Architecture
* Docker
* Spring Security
* Spring Cloud
* PostgreSQL
* MongoDB
* MapStruct
* Lombok

## Architecture
This project follows a Microservices Architecture, where different services handle specific functionalities independently. Each service is developed, deployed, and scaled separately, ensuring better maintainability and flexibility.

### Microservices Overview
api-gateway: Acts as an entry point for client requests, routing them to appropriate microservices.
attachment-service: Manages file uploads and storage.
auth-service: Handles authentication, authorization, and user management.
comment-service: Manages user comments and related operations.
config-server: Centralized configuration management for all services.
eureka-server: Service discovery, allowing services to locate and communicate with each other.
project-service: Manages project-related functionalities.
task-service: Handles tasks and related operations.

### Microservices Architecture Principles
Service Independence: Each service operates independently, ensuring high availability and easy maintenance.
Scalability: Services can be scaled separately based on demand.
Resilience: Failures in one service do not affect the entire system.
Service Discovery: Eureka Server enables dynamic service registration and discovery.
Centralized Configuration: Config Server ensures consistent configuration across all services.

## API Documentation
You can access the API documentation from the link [here](https://drive.google.com/file/d/1B0eDaCQA7BzJtjLg2mNvj1gK-nF5rbNy/view?usp=drive_link)

## Contact

<table style="border-collapse: collapse; width: 100%;">
  <tr>
    <td style="padding-right: 10px;">Yusuf Mücahit Solmaz - <a href="mailto:yusufmucahitsolmaz@gmail.com">yusufmucahitsolmaz@gmail.com</a></td>
    <td>
      <a href="https://www.linkedin.com/in/yusuf-mucahit-solmaz/" target="_blank">
        <img src="https://img.shields.io/badge/linkedin-%231E77B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white" alt="linkedin" style="vertical-align: middle;" />
      </a>
    </td>
  </tr>
</table>

## Licence
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
