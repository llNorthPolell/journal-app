import NextAuth from 'next-auth'
import { JWT } from "next-auth/jwt"

declare module 'next-auth' {
  interface Session {
    user: User
    
  }

  interface Account {
    expires_in: number
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    access_token?: Account.access_token;
    id_token? : Account.id_token;
    expires: number;
    refresh_token: Account.refresh_token;
  }
}