/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  experimental: {
    serverActions: true
  },
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'firebasestorage.googleapis.com',
        port: '',
        pathname: '/v0/b/journal-app-75df1.appspot.com/o/**'
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '9199',
        pathname: '/v0/b/journal-app-75df1.appspot.com/o/**'
      }

    ]
  }
}

module.exports = nextConfig
