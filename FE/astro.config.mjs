import { defineConfig } from "astro/config";
import tailwind from "@astrojs/tailwind";
import { astroImageTools } from "astro-imagetools";
import sitemap from "@astrojs/sitemap";
import react from "@astrojs/react";

import svelte from "@astrojs/svelte";

import vercel from "@astrojs/vercel/serverless";

// https://astro.build/config
export default defineConfig({
    // site: "https://stargazers.club", todo
    integrations: [tailwind(), astroImageTools, sitemap(), react(), svelte()],
    output: "server",
    adapter: vercel(),
});
