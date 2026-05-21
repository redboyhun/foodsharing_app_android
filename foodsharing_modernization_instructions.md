# Foodsharing App Modernization PRD

## Visual Direction: "Modern, Fresh & Trustworthy"
The goal is to transition from the current utilitarian, slightly dated look to a modern, approachable interface that emphasizes community and sustainability.

### Core Visual Principles
1. **Nature-Inspired Palette**: Retain the signature green but move toward a more vibrant, balanced palette. Use a deep forest green for primary actions, a minty light green for accents, and warm neutrals (cream/off-white) for backgrounds to avoid the clinical pure white.
2. **Soft Geometry**: Replace sharp corners with generous border radii (16px–24px) for cards and buttons to create a friendlier, "organic" feel.
3. **Intentional Whitespace**: Increase padding significantly. Let elements "breathe" to reduce cognitive load, especially in the message list.
4. **Depth & Shadow**: Use subtle, soft shadows (elevations) instead of borders to define hierarchy. This creates a layered, modern "glassmorphism" or "soft-ui" aesthetic.
5. **Human-Centric Typography**: Switch to a modern, rounded sans-serif (e.g., Quicksand or Montserrat) for a friendly tone. Use a clear type scale to distinguish between sender names and message snippets.

---

## Screen-Specific Instructions

### 1. Brand Identity & Logo
- **Logo**: Refine the "fork-and-leaf" icon. Simplify the shapes into a clean, minimalist vector mark. Use the updated forest green.
- **Splash/Login**: The logo should be prominent but not overwhelming. Use a subtle gradient or a soft pattern in the background.

### 2. Login Screen (`{{DATA:IMAGE:IMAGE_2}}`)
- **Background**: Soft off-white or a very light mint tint.
- **Inputs**: Rounded "pill" shaped inputs or soft-cornered rectangles. Use subtle borders that only highlight on focus.
- **Button**: A high-contrast "Forest Green" primary button with rounded corners and a soft drop shadow.
- **Typography**: "Foodsharing" should be in a bold, friendly weight. Add a secondary tagline in a lighter gray.

### 3. Messages List (`{{DATA:IMAGE:IMAGE_1}}`)
- **Header**: Replace the solid green bar with a clean, white header featuring a large, bold title. Use the green as an accent (e.g., for the active tab indicator).
- **Message Cards**: Remove the thin borders. Use a white card with a very soft shadow on a light neutral background.
- **Avatars**: Add circular placeholder avatars or initials for each sender to make the list feel personal.
- **Metadata**: Move timestamps to a lighter weight and smaller font size, perhaps tucked into the top-right of the card.
- **Bottom Navigation**: Use a clean, floating or docked bar with refined line-art icons. The active state should be the primary green.

---

## Implementation Instructions for Stitch
1. **Design System**: Create a design system named "Foodsharing Modern" using a forest green (#2D5A27), mint (#A8E6CF), and warm white (#FAFAF5). Use 'Outfit' or 'Quicksand' as the primary font.
2. **Components**: Predict components for a persistent Bottom Nav (Icons: Home, Messages, Map, Profile) and a "MessageCard" component with avatar, name, snippet, and time.
3. **Screen Generation**: 
   - Generate the Login screen first to set the brand tone.
   - Generate the Messages List using the predicted cards.
   - Generate a "Message Thread" view to complete the core flow.
   - Generate a "Food Map/Discovery" screen to show off the modern UI in a data-rich context.