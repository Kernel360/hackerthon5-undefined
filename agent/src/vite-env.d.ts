/// <reference types="vite/client" />

// eslint-disable-next-line @typescript-eslint/no-empty-object-type
interface ImportMetaEnv {
    // Put VITE_ prefixed environment variables here to make them available in the app via `import.meta.env`
    // e.g:
    // readonly VITE_APP_TITLE: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
