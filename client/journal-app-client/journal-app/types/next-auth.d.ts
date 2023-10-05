import NextAuth from 'next-auth'

declare module 'next-auth' {
  interface Session {
    access_token?: string
  }

  
  interface User {
    access_token: string
   & DefaultSession["user"];
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    access_token?: string;
  }
}