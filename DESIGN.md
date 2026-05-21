---
name: Foodsharing Modern
colors:
  surface: '#fbf9f8'
  surface-dim: '#dbd9d9'
  surface-bright: '#fbf9f8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f3f3'
  surface-container: '#efeded'
  surface-container-high: '#eae8e7'
  surface-container-highest: '#e4e2e2'
  on-surface: '#1b1c1c'
  on-surface-variant: '#42493e'
  inverse-surface: '#303030'
  inverse-on-surface: '#f2f0f0'
  outline: '#72796e'
  outline-variant: '#c2c9bb'
  surface-tint: '#3b6934'
  primary: '#154212'
  on-primary: '#ffffff'
  primary-container: '#2d5a27'
  on-primary-container: '#9dd090'
  inverse-primary: '#a1d494'
  secondary: '#2c6956'
  on-secondary: '#ffffff'
  secondary-container: '#aeedd5'
  on-secondary-container: '#316d5b'
  tertiary: '#383a37'
  on-tertiary: '#ffffff'
  tertiary-container: '#4f514d'
  on-tertiary-container: '#c3c3bf'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#bcf0ae'
  primary-fixed-dim: '#a1d494'
  on-primary-fixed: '#002201'
  on-primary-fixed-variant: '#23501e'
  secondary-fixed: '#b1efd8'
  secondary-fixed-dim: '#96d3bd'
  on-secondary-fixed: '#002118'
  on-secondary-fixed-variant: '#0d503f'
  tertiary-fixed: '#e2e3de'
  tertiary-fixed-dim: '#c6c7c2'
  on-tertiary-fixed: '#1a1c19'
  on-tertiary-fixed-variant: '#454744'
  background: '#fbf9f8'
  on-background: '#1b1c1c'
  surface-variant: '#e4e2e2'
  surface-warm: '#FAFAF5'
  surface-pure: '#FFFFFF'
  forest-deep: '#2D5A27'
  mint-accent: '#A8E6CF'
  text-muted: '#808080'
typography:
  display-lg:
    fontFamily: Outfit
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Outfit
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Outfit
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
  headline-md:
    fontFamily: Outfit
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Outfit
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Outfit
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Outfit
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Outfit
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Outfit
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  xs: 4px
  sm: 12px
  md: 24px
  lg: 32px
  xl: 48px
  gutter: 16px
  margin-mobile: 20px
  margin-desktop: 64px
---

## Brand & Style

The design system is rooted in the philosophy of **Sustainable Community**. It transitions from a utility-first application to an organic, human-centric platform. The aesthetic is defined by **Soft Minimalism**, utilizing nature-inspired tones and generous whitespace to foster trust and reduce cognitive load.

The UI should evoke feelings of freshness, reliability, and warmth. By replacing harsh lines with soft geometry and subtle depth, the system creates an approachable environment that encourages food sharing and community interaction. It is modern yet grounded, avoiding the sterile "clinical" look of traditional enterprise software in favor of a vibrant, living ecosystem.

## Colors

The palette is inspired by natural landscapes. **Forest Green** serves as the anchor for primary actions and brand presence, signaling growth and sustainability. **Mint** acts as a fresh accent for highlights and background tints, while **Warm White** is the primary surface color to provide a comfortable, non-glaring backdrop.

**Pure White** is reserved for high-elevation elements like message cards and headers to create a clear layering effect against the warm background. Secondary text uses a soft gray to maintain hierarchy without introducing harsh contrast.

## Typography

This design system uses **Outfit** for its friendly, rounded geometric terminals which mirror the soft corners of the UI components. The typography scale prioritizes legibility and clear hierarchy. 

Headings are bold and impactful, while body text uses generous line heights to enhance readability during long interactions. Small metadata (like timestamps or secondary tags) uses a reduced weight and size but maintains the rounded character of the font family to ensure the brand voice remains consistent across all scales.

## Layout & Spacing

The layout philosophy follows a **Fluid Grid** with intentional, breathable whitespace. Elements are organized using an 8px base unit, but padding is intentionally increased—particularly in lists and cards—to reduce visual noise.

- **Mobile:** 4-column grid with 20px margins and 16px gutters.
- **Desktop:** 12-column grid with 64px margins. Content is often contained in a max-width container (1200px) to maintain focus.
- **Rhythm:** Vertical spacing between cards should be generous (16px to 24px) to emphasize their independent "floating" nature.

## Elevation & Depth

Hierarchy is established through **Tonal Layering** and **Soft Shadows** rather than borders. The base canvas is `surface-warm`. Elements like cards and headers are placed on `surface-pure` and elevated using diffused, low-opacity shadows.

- **Level 1 (Cards/Inputs):** Subtle 4px blur, 5% opacity black shadow.
- **Level 2 (Dropdowns/Modals):** 12px blur, 10% opacity black shadow.
- **Interaction:** Buttons may use a slight lift (increased shadow spread) on hover to provide tactile feedback. 

Borders are strictly avoided except for high-accessibility focus states, where a 2px Mint or Forest Green outline may be used.

## Shapes

The "Soft Geometry" pillar dictates a high degree of roundedness across the system. 
- **Cards & Containers:** Use a standard radius of 20px (ranging 16px-24px depending on scale).
- **Buttons & Inputs:** Utilize **Pill shapes** (fully rounded ends) to emphasize the approachable, friendly nature of the brand.
- **Avatars:** Strictly circular.
- **Icons:** Should feature rounded caps and corners to match the surrounding UI components.

## Components

### Buttons
Primary buttons are Forest Green with white text, utilizing a pill shape. Secondary buttons use a Mint background with Forest Green text. All buttons should have a minimum height of 48px to ensure touch-friendly targets.

### Cards
Cards are the primary container for the "Foodsharing Modern" system. They are Pure White (`#FFFFFF`) with a 20px border radius and a soft shadow. Padding within cards should be a minimum of 24px to ensure content has room to breathe.

### Input Fields
Inputs are pill-shaped with a light gray or Mint-tinted background. They do not have visible borders by default; depth is created through a slight inner shadow or subtle tonal difference from the background. Labels should be placed above the field in `label-md` styling.

### Lists & Messaging
Message list items are styled as individual cards rather than a continuous list with dividers. This reinforces the "soft geometry" and makes each conversation feel like a distinct, tangible object.

### Chips
Used for categories (e.g., "Vegetables", "Bakery"). Chips use the Mint accent color with a high level of roundedness (pill-shaped) and small font size.

### Navigation
The bottom navigation bar uses a Pure White background with Forest Green for the active state. Icons are refined line-art with a 2px stroke width and rounded joins.