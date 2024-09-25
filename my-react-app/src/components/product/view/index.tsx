import * as React from "react";
import { useState, useEffect } from "react";
import { IProductItem } from "./types";
import { Card, Col, Row, Modal } from "antd";
import axios from "axios";

const ProductViewPage: React.FC = () => {
  const [products, setProducts] = useState<IProductItem[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<IProductItem | null>(null);
  const [previewOpen, setPreviewOpen] = useState<boolean>(false);
  const [previewImage, setPreviewImage] = useState<string>('');

  useEffect(() => {
    const fetchProducts = async () => {
      const result = await axios.get<IProductItem[]>("http://localhost:8888/api/product");
      setProducts(result.data);
    };

    fetchProducts();
  }, []);

  const handleImagePreview = (imageUrl: string) => {
    setPreviewImage(imageUrl);
    setPreviewOpen(true);
  };

  return (
    <>
      <Row gutter={16}>
        {products.map(product => (
          <Col span={8} key={product.id}>
            <Card
              title={product.name}
              onClick={() => setSelectedProduct(product)}
            >
              <p>{product.description}</p>
              <p>Ціна: {product.price}</p>
              <p>Знижка: {product.discount}%</p>
            </Card>
          </Col>
        ))}
      </Row>

      {selectedProduct && (
        <Modal
          open={true}
          title={selectedProduct.name}
          onCancel={() => setSelectedProduct(null)}
          footer={null}
        >
          <p>{selectedProduct.description}</p>
          <Row gutter={16}>
            {selectedProduct.images.map(image => (
              <Col span={8} key={image}>
                <Card
                  cover={<img alt="example" src={image} onClick={() => handleImagePreview(image)} />}
                />
              </Col>
            ))}
          </Row>
        </Modal>
      )}

      <Modal open={previewOpen} title="Image Preview" footer={null} onCancel={() => setPreviewOpen(false)}>
        <img alt="example" style={{ width: '100%' }} src={previewImage} />
      </Modal>
    </>
  );
};

export default ProductViewPage;
