---
name: git-commit
description: Automates git commit using conventional commits + gitmoji and breaks down commits by feature. Use this whenever you are instructed to commit changes to a project.
---

# Git Commit Automation Skill

This skill enforces a standard commit pattern using **Conventional Commits** combined with **Gitmoji**, and requires that commits be **broken down specifically by feature** or scope (e.g. data, domain, presentation, core, album, player, song).

## 1. Commit Breakdown Strategy

Do not lump all changes into a single monolithic commit. 
You MUST analyze the staged (or unstaged) changes and logically break them down by feature, layer, or scope.

For example, if you modified the `album` screen and the `player` screen, create two separate commits:
1. `feat(album): ...`
2. `feat(player): ...`

If you are working on a vertical slice, separate by layer if appropriate (e.g., `feat(data): ...`, `feat(domain): ...`, `feat(presentation): ...`).

## 2. Commit Message Format

Each commit message must strictly follow this format:
`<gitmoji> <type>(<scope>): <subject>`

- `<gitmoji>`: An emoji representing the intent of the change.
- `<type>`: The conventional commit type (feat, fix, chore, docs, refactor, test, etc.).
- `<scope>`: The feature, layer, or module being changed (e.g., `album`, `song`, `presentation`, `domain`, `data`, `core`, `multi-module`, `readme`).
- `<subject>`: A short, imperative tense description of the change in English.

## 3. Gitmojis & Types Mapping

Use the following specific emojis based on the project's established conventions:

- ✨ `feat`: A new feature or logic addition (e.g., `✨ feat(domain): add UpdateLastPlayedSong use case`)
- 💄 `feat`: UI/Presentation additions or changes (e.g., `💄 feat(presentation): add MiniPlayerBar component`)
- 🐛 `fix`: A bug fix (e.g., `🐛 fix(player): resolve crash on resume`)
- ♻️ `refactor`: Code refactoring without adding features or fixing bugs (e.g., `♻️ refactor(album): clean up presentation`)
- 💥 `refactor`: Breaking changes or major structural changes (e.g., `💥 refactor(multi-module): modularize project`)
- 🙈 `chore`: Ignoring files, `.gitignore` updates (e.g., `🙈 chore(gitignore): add /release`)
- 🔧 `chore`: Updating dependencies, build scripts, configuration
- 🎉 `chore`: Initial project creation or scaffolding (e.g., `🎉 chore(core): initial project setup`)
- 📝 `docs`: Documentation changes (e.g., `📝 docs(readme): update APK download link`)
- 📸 `docs`: Visual documentation / screenshots (e.g., `📸 docs(screenshots): update app screenshots`)
- ✅ `test`: Adding or updating tests (e.g., `✅ test(ui): add automated tests for AlbumScreen`)

## 4. Execution Steps

When asked to commit changes:
1. Review the changes using `git status` and `git diff`.
2. Group the modified files logically by feature or architectural layer.
3. For each group, stage the specific files (`git add <files>`).
4. Commit using the `git commit -m "<gitmoji> <type>(<scope>): <subject>"` format.
5. Repeat steps 3-4 until all intended changes are logically committed.
