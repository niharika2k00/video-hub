### 🌐 Requirements:

1. **Authentication**:
   - Create routes and components for **Register** and **Sign In**
   - Use form validation and basic authentication logic (JWT or token-based)

2. **User Profile**:
   - Profile page should include:
    - User image, Name, contact number, email, about section
   - Editable sections if possible

3. **Dashboard**:
   - Users can upload videos (via form with some fields like category, description, etc and the video file can be drag and drop or taken file as input)
   - Once uploaded, videos appear in a grid/list
   - Each video is shown with:
     - Thumbnail
     - Title or filename
     - A **delete icon** (overlay style on hover)

4. **Video Player Page**:
   - Clicking a video opens a full video player
   - Use a third-party package (like `react-player`, `video.js`, or `plyr-react`)
   - Player must support:
     - Volume control
     - Playback speed selection
     - Resolution switcher (144p, 360p, 720p, 1080p)
     - Fullscreen and mini-player mode

5. **Design**:
   - A **soothing, modern UI** with cool-toned color palette
   - Fully **responsive**, clean layout (mobile, tablet, desktop)
   - Use **CSS Modules** or **SCSS** (not styled-components)

6. **State Management**:
   - Use **Zustand** or **Context API** for user session, video list, upload status
   - Make the logic **decoupled** from UI where possible

7. **Project Structure**:
   - Modular, extensible architecture
   - Follow **DRY principles**, with reusable components (buttons, inputs, modals)
   - Centralized file for constants like colors, spacing, breakpoints (`/src/constants/theme.js`)
   - Routes organized with `react-router-dom` v6
   - Pages under `/src/pages`, components under `/src/components`, hooks under `/src/hooks`, context under `/src/context`, services (API calls) under `/src/services`

8. **Naming & Conventions**:
   - Use consistent, clean naming
   - Use `camelCase` for variables/functions, `PascalCase` for components
   - Use environment variables (`.env`) for backend API URLs

9. **Responsiveness**:
   - Design must support adaptive layouts using CSS flex/grid and media queries
   - Navbar collapses on small screens
   - Dashboard grid adjusts for different screen widths

10. **Bonus (Optional)**:
    - Add a basic toast system for showing success/error (e.g., `react-toastify`)
    - Show processing status if the video is still being transcoded

### 📁 Output:
- Generate the full ReactJS project with structured folders
- Focus on readability and reusability
- Do **not** include fake mock data — assume real API integrations
- Use `create-react-app` or `Vite` as base (preferably Vite for performance)
