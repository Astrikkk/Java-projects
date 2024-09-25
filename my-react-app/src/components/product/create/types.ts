export interface ICategoryCreate {
    name: string;
    description: string;
    image: File;
  }
  
  export interface IProductCreate {
    name: string;
    description: string;
    price: number;
    discount: number;
    categoryId: number;
    images: File[];
  }
  