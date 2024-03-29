import * as admin from 'firebase-admin';
import { URL } from 'url';
import { join } from 'path';
import { tmpdir } from 'os';

const storage = admin.storage();

export function getTmpPath(filename: string) {
  return join(tmpdir(), filename);
}

export function isUrl(url: string) {
  try {
    const { protocol } = new URL(url);
    return ['http:', 'https:'].includes(protocol);
  } catch (err) {
    return false;
  }
}

export async function upload(path: string, destination: string) {
  const [file] = await storage.bucket().upload(path, { destination, public: true });
  const [metadata] = await file.getMetadata();
  return metadata.mediaLink;
}

export async function move(source: string, destination: string) {
  const [file] = await storage.bucket().file(source).move(destination);
  await file.makePublic();
  const [metadata] = await file.getMetadata();
  return metadata.mediaLink;
}

export async function download(source: string) {
  const [file] = await storage.bucket().file(source).download();
  return file;
}
