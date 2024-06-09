# Web Page Categorization System

## Overview

This project implements a system for categorizing web pages based on predefined keyword categories using Java. The system utilizes the Aho-Corasick algorithm for efficient multi-pattern keyword matching, leveraging the `org.ahocorasick` library and `Jsoup` for HTML parsing and text extraction.

## Features

- **Keyword-Based Categorization**: Classify web pages into categories based on predefined keywords.
- **Efficient Multi-Pattern Matching**: Use the Aho-Corasick algorithm to efficiently search for multiple keywords within the text content of web pages.
- **HTML Parsing and Cleaning**: Fetch and clean web page content using `Jsoup` to remove unnecessary HTML elements and URLs.

## Trie Data Structure

The system uses the Trie data structure from the `org.ahocorasick` library to store and search for keywords efficiently.

- **categoryTries**: A map that associates each category name with a corresponding Trie built from its keywords.

## Building the Trie

**Time Complexity**: Building a Trie for a single category with `k` keywords, each of average length `l`, takes `O(k * l) * M`, where `M` is the number of categories.

## Setup and Usage

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle for building the project

### Project Structure

- **src/main/java**: Contains the main source code.
- **src/test/java**: Contains unit tests.

### Building the Project

To build the project using Gradle, run the following command:

```sh
./gradlew clean build
