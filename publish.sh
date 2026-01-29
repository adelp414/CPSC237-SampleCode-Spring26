#!/usr/bin/env bash
set -euo pipefail

# Configuration
ORG="YZhang-CS-Teaching"
REPO_NAME="CPSC237-SampleCode-Spring26"
GITHUB_USER="${GITHUB_USER}"
TOKEN="${GITHUB_TOKEN}"

echo "=== Pushing current directory to $ORG/$REPO_NAME"

# Create .gitignore if it doesn't exist
if [[ ! -f .gitignore ]]; then
  echo "Creating .gitignore..."
  cat > .gitignore << 'EOF'
.env
*.swp
.DS_Store
a.out
*~
EOF
fi

# Make sure .env is in .gitignore (in case .gitignore existed without it)
if ! grep -q "^\.env$" .gitignore; then
  echo "Adding .env to .gitignore..."
  echo ".env" >> .gitignore
fi

# Check if repo exists, create if not
if ! git ls-remote "https://$TOKEN@github.com/$ORG/$REPO_NAME.git" &>/dev/null; then
  echo "Creating repository $REPO_NAME in organization $ORG..."
  curl -s -X POST \
    -H "Authorization: token $TOKEN" \
    -H "Accept: application/vnd.github+json" \
    https://api.github.com/orgs/$ORG/repos \
    -d "{
      \"name\": \"$REPO_NAME\",
      \"private\": false,
      \"description\": \"Sample code for CPSC237 Spring 2026\"
    }" >/dev/null
  sleep 2
fi

# Initialize git if not already initialized
if [[ ! -d .git ]]; then
  echo "Initializing git repository..."
  git init
  git branch -M main
fi

# Set or update remote
if git remote | grep -q "^origin$"; then
  echo "Updating remote URL..."
  git remote set-url origin "https://$TOKEN@github.com/$ORG/$REPO_NAME.git"
else
  echo "Adding remote..."
  git remote add origin "https://$TOKEN@github.com/$ORG/$REPO_NAME.git"
fi

# Stage all files (will respect .gitignore)
echo "Staging files..."
git add -A

# Verify .env is not staged
if git diff --cached --name-only | grep -q "^\.env$"; then
  echo "ERROR: .env is being staged! This should not happen."
  echo "Please check your .gitignore file."
  exit 1
fi

# Commit changes
if git diff-index --quiet HEAD -- 2>/dev/null; then
  echo "No changes to commit"
else
  echo "Committing changes..."
  git commit -m "Update sample code"
fi

# Push to GitHub
echo "Pushing to GitHub..."
git push -u origin main

echo "✅ Done! Code pushed to https://github.com/$ORG/$REPO_NAME"