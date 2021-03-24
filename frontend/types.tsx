export type integer = number;
export type date = Date; // JS Date
export type pk = unknown;
export type fk = unknown;

// Response type start

export interface IResponse {
  success: boolean;
  status: integer;
  comment?: string;
}

export interface IDataResponse<Data> extends IResponse {
  data: Array<Data>;
}

export interface IModel<Fields> {
  model: string;
  pk: integer;
  fields: Fields;
}

// Response type end

// Database(and data) type start

export interface IDocument {
  title: string & pk;
  datetime?: Date;
  profile?: integer;
  image?: string;
  content?: string;
  profileNumber?: IProfile & fk;
  isShow?: boolean;
}

export interface IProfile {
  profileNumber: integer & pk;
  bestPlace?: string;
  bestDocument?: string;
  name?: string;
  photo?: string;
  private?: boolean;
  clientNumber: IClient & fk;
}

export interface IFollower {
  following: integer;
  follower: integer;
  sendDate?: Date;
  acceptDate?: Date;
  status?: boolean;
}

export interface IPlace {
  placeNum: integer & pk;
  name?: string;
  rate?: string;
  photo?: string;
  profileNumber: IProfile & fk;
  date: integer;
  isShow: boolean;
}

export interface IPlaceMap {
  placeMapPk: string;
  photo?: string;
  placeNum: IPlace & fk;
}
export interface ILike {
  postNumber: IDocument;
  profileNumber: IProfile;
  like?: boolean;
  date?: Date;
}

export interface IRoute {
  postNumber: IDocument;
  placeNumber: IPlace;
  routeOrder: integer;
}

export interface IRouteMap {
  routeMapPk: string;
  photo?: string;
  routeMapNumber: IRoute & fk;
}

export interface IClient {
  clientNumber: integer & pk;
  id: string;
  password: string;
  email: string;
  address?: string;
}

export interface IClientInfo {
  clientNumber: IClient;
  visitedCount?: integer;
  firstVisit?: date;
  lastVisit?: date;
}

// Database type end
