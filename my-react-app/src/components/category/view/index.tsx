import React, { useEffect, useState } from "react";
import { Card, Col, Row, Modal, Spin, Empty } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import axios from "axios";

interface ICategory {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
}

const CategoryViewPage: React.FC = () => {
  const [categories, setCategories] = useState<ICategory[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [previewOpen, setPreviewOpen] = useState<boolean>(false);
  const [previewImage, setPreviewImage] = useState<string>("");
  const [previewTitle, setPreviewTitle] = useState<string>("");

  useEffect(() => {
    const fetchCategories = async () => {
      const url = "http://localhost:8888/api/category";
      try {
        const response = await axios.get<ICategory[]>(url);
        setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchCategories();
  }, []);

  const handlePreview = (image: string, title: string) => {
    setPreviewImage(image);
    setPreviewTitle(title);
    setPreviewOpen(true);
  };

  return (
    <div className="py-12">
      <div className="mx-auto max-w-7xl px-6 lg:px-24">
        <p className="text-center text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
          Перегляд категорій
        </p>

        {loading ? (
          <div className="text-center mt-10">
            <Spin size="large" />
          </div>
        ) : categories.length === 0 ? (
          <Empty description="Немає категорій для відображення" />
        ) : (
          <Row gutter={[16, 16]} className="mt-8">
            {categories.map((category) => (
              <Col key={category.id} xs={24} sm={12} md={8} lg={6}>
                <Card
                  hoverable
                  cover={
                    <img
                      alt={category.name}
                      src={category.imageUrl}
                      onClick={() =>
                        handlePreview(category.imageUrl, category.name)
                      }
                    />
                  }
                >
                  <Card.Meta
                    title={category.name}
                    description={category.description}
                  />
                </Card>
              </Col>
            ))}
          </Row>
        )}
      </div>

      <Modal
        open={previewOpen}
        title={previewTitle}
        footer={null}
        onCancel={() => setPreviewOpen(false)}
      >
        <img alt="Preview" style={{ width: "100%" }} src={previewImage} />
      </Modal>
    </div>
  );
};

export default CategoryViewPage;
